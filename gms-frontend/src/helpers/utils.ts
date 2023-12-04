export const capacityPercent = (weight: number, capacity: number) => {
  const percent = weight / capacity;
  if (percent >= 0.5 && percent < 0.7) {
    return 1;
  } else if (percent >= 0.7) {
    return 2;
  }
  return 0;
};

export const isObjKey = <T extends object>(
  key: PropertyKey,
  obj: T
): key is keyof T => {
  return key in obj;
};
