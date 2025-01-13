use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::collections::HashMap;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::iter::zip;

const DAY: &str = "11";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "125 17";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn blink(numbers: &Vec<usize>) -> Vec<Vec<usize>> {
        let mut new = vec![];
        numbers.iter().for_each(|x: &usize| {
            if *x == 0 {
                new.push(vec![1]);
            } else {
                let s = x.to_string();
                if s.len() % 2 == 0 {
                    let (left, right) = s.split_at(s.len() / 2);
                    new.push(vec![
                        left.parse::<usize>().unwrap(),
                        right.parse::<usize>().unwrap(),
                    ]);
                } else {
                    new.push(vec![x * 2024]);
                }
            }
        });
        new
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let mut numbers = reader
            .lines()
            .next()
            .unwrap()?
            .split_whitespace()
            .map(|x| x.parse::<usize>().unwrap())
            .collect_vec();

        for _ in 0..25 {
            numbers = blink(&numbers).iter().flatten().cloned().collect_vec();
        }

        let answer = numbers.len();
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(55312, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let mut numbers = reader
            .lines()
            .next()
            .unwrap()?
            .split_whitespace()
            .map(|x| (x.parse::<usize>().unwrap(), 1 as usize))
            .collect::<HashMap<_, _>>();

        for _ in 0..75 {
            let keys = numbers.keys().copied().collect_vec();
            let new = blink(&keys);
            let mut new_numbers: HashMap<usize, usize> = HashMap::new();
            for (k, new) in zip(keys, new) {
                let mult = numbers[&k];
                new.iter().for_each(|x| {
                    if new_numbers.contains_key(x) {
                        new_numbers.insert(*x, mult + new_numbers.get(x).unwrap());
                    } else {
                        new_numbers.insert(*x, mult);
                    }
                })
            }
            numbers = new_numbers;
        }

        let answer = numbers.values().sum::<usize>();

        Ok(answer)
    }

    // assert_eq!(55312, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
